package br.com.regulei.backend.persistence;


import br.com.regulei.backend.menstrualrecord.entity.MenstrualRecord;
import br.com.regulei.backend.menstrualrecord.repository.MenstrualRecordRepository;
import br.com.regulei.backend.user.entity.User;
import br.com.regulei.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PersistenteIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenstrualRecordRepository menstrualRecordRepository;

    @Test
    void devePersistirUsuarioComEmailNormalizadoEHash(){
        User user = new User("Gabriel", "GABRIEL@gmail.com", "$2a$10$hashfake");
        User saved = userRepository.saveAndFlush(user);


        assertNotNull(saved.getId());
        assertEquals("gabriel@gmail.com", saved.getEmail());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

    }

    @Test
    void naoDevePermitirEmailDuplicado(){
        userRepository.saveAndFlush(new User("Ana", "ana@gmail.com", "$2a$10$hashfake"));
        User userDuplicado = new User("Outra Ana", "ANA@gmail.com", "$2a$10$hash2");
        // Garante que o banco bloqueia a inserção duplicada
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(userDuplicado);
        });
    }

    @Test
    void devePersistirRegistrosMenstruaisEIsolarPorUsuaria() {
        User ana = userRepository.saveAndFlush(new User("Ana", "ana@exemplo.com", "$2a$10$hash1"));
        User bia = userRepository.saveAndFlush(new User("Bia", "bia@exemplo.com", "$2a$10$hash2"));

        menstrualRecordRepository.saveAndFlush(
                new MenstrualRecord(LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 14), ana)
        );
        menstrualRecordRepository.saveAndFlush(
                new MenstrualRecord(LocalDate.of(2026, 10, 8), null, ana)
        );

        menstrualRecordRepository.saveAndFlush(
                new MenstrualRecord(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), bia)
        );

        List<MenstrualRecord> registrosAna =
                menstrualRecordRepository.findAllByUserIdOrderByStartDateDesc(ana.getId());

        assertEquals(2, registrosAna.size());

        assertEquals(LocalDate.of(2026, 10, 8), registrosAna.get(0).getStartDate());
        assertNull(registrosAna.get(0).getEndDate());

        assertEquals(LocalDate.of(2026, 9, 10), registrosAna.get(1).getStartDate());
        assertEquals(LocalDate.of(2026, 9, 14), registrosAna.get(1).getEndDate());
    }

    @Test
    void naoDevePermitirDataFinalAnteriorADataInicial() {
        User ana = userRepository.saveAndFlush(new User("Ana", "ana@exemplo.com", "$2a$10$hash1"));
        MenstrualRecord registroInvalido = new MenstrualRecord(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 9),
                ana
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            menstrualRecordRepository.saveAndFlush(registroInvalido);
        });
    }

}
