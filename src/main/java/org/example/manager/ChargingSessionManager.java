package org.example.manager;

import org.example.model.ChargingSession;
import org.example.model.ChargingPoint;
import org.example.model.Customer;
import org.example.model.enums.SessionStatus;
import org.example.model.enums.OperationalStatus;
import java.util.*;

public class ChargingSessionManager {
    private static ChargingSessionManager instance;
    private Map<String, ChargingSession> activeSessions;
    private Map<String, ChargingSession> allSessions;

    private ChargingSessionManager() {
        activeSessions = new HashMap<>();
        allSessions = new HashMap<>();
    }

    public static synchronized ChargingSessionManager getInstance() {
        if (instance == null) {
            instance = new ChargingSessionManager();
        }
        return instance;
    }

    public ChargingSession startSession(ChargingPoint chargingPoint, Customer customer) {
        ChargingSession session = new ChargingSession(chargingPoint.getId(), customer.getId());

        activeSessions.put(session.getId(), session);
        allSessions.put(session.getId(), session);

        chargingPoint.setStatus(OperationalStatus.IN_USE);

        return session;
    }

    public void stopSession(ChargingSession session) {
        session.end();
        activeSessions.remove(session.getId());

        ChargingPointManager cpManager = ChargingPointManager.getInstance();
        ChargingPoint cp = cpManager.getChargingPointById(session.getChargingPointId());
        if (cp != null) {
            cp.setStatus(OperationalStatus.AVAILABLE);
        }
    }

    public ChargingSession getSessionById(String sessionId) {
        return allSessions.get(sessionId);
    }

    public ChargingSession getActiveSessionForCustomer(String customerId) {
        for (ChargingSession session : activeSessions.values()) {
            if (customerId.equals(session.getCustomerId())) {
                return session;
            }
        }
        return null;
    }

    public boolean isSessionActive(ChargingSession session) {
        return SessionStatus.ACTIVE.equals(session.getStatus());
    }

    public boolean isSessionActive(String sessionId) {
        ChargingSession session = getSessionById(sessionId);
        return session != null && SessionStatus.ACTIVE.equals(session.getStatus());
    }

    public String getSessionConfirmation(String sessionId) {
        return "Charging session " + sessionId + " has started successfully.";
    }

    public String getSessionStatus(String sessionId) {
        ChargingSession session = getSessionById(sessionId);
        if (session != null) {
            return "Session " + sessionId + " is " + session.getStatus().getDisplayName() +
                    ". Charging time: " + session.getChargingTime() + " minutes, " +
                    "Paused for: " + session.getPauseDuration() + " minutes";
        }
        return "Session not found";
    }

    public void pauseSession(String sessionId, int minutes) {
        ChargingSession session = getSessionById(sessionId);
        if (session != null) {
            session.pause(minutes);
        }
    }

    public void resumeSession(String sessionId) {
        ChargingSession session = getSessionById(sessionId);
        if (session != null) {
            session.resume();
        }
    }

    public List<ChargingSession> getActiveSessions() {
        return new ArrayList<>(activeSessions.values());
    }
}
