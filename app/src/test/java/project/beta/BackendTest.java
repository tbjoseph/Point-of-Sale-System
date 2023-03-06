package project.beta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BackendTest {
    @Spy
    private BackendDAO dao;

    @Test
    void testLogin() {
        assertTrue(dao.login("admin", "admin"));
        assertFalse(dao.login("admin", "wrong"));
        assertFalse(dao.login(null, null));
    }
}
