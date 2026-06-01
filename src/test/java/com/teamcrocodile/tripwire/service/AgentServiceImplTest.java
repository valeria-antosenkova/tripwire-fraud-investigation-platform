package com.teamcrocodile.tripwire.service;

import com.teamcrocodile.tripwire.dao.AgentDao;
import com.teamcrocodile.tripwire.model.Agent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentServiceImplTest {

    @Mock
    private AgentDao agentDao;

    @Test
    void changePassword_updatesStoredHashWhenCurrentPasswordMatches() {
        AgentServiceImpl service = new AgentServiceImpl(agentDao);

        Agent agent = new Agent();
        agent.setId(1);
        agent.setPass_hash("{noop}tripwire123");

        when(agentDao.getAgentById(1)).thenReturn(agent);

        service.changePassword(1, "tripwire123", "newPassword123");

        ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);
        verify(agentDao).updatePasswordHash(eq(1), hashCaptor.capture());

        String storedHash = hashCaptor.getValue();
        assertFalse(storedHash.equals("newPassword123"));
        assertFalse(storedHash.equals("tripwire123"));
    }

    @Test
    void changePassword_throwsWhenCurrentPasswordIsWrong() {
        AgentServiceImpl service = new AgentServiceImpl(agentDao);

        Agent agent = new Agent();
        agent.setId(1);
        agent.setPass_hash("{noop}tripwire123");

        when(agentDao.getAgentById(1)).thenReturn(agent);

        assertThrows(SecurityException.class,
                () -> service.changePassword(1, "bad-password", "newPassword123"));
        verify(agentDao, never()).updatePasswordHash(anyInt(), anyString());
    }

    @Test
    void changePassword_throwsWhenNewPasswordTooShort() {
        AgentServiceImpl service = new AgentServiceImpl(agentDao);

        assertThrows(IllegalArgumentException.class,
                () -> service.changePassword(1, "tripwire123", "short"));
        verify(agentDao, never()).getAgentById(anyInt());
        verify(agentDao, never()).updatePasswordHash(anyInt(), anyString());
    }
}

