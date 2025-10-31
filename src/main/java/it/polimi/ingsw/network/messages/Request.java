package it.polimi.ingsw.network.messages;

/**
 * This enum contains all the message type available and used by the server and clients.
 */
public enum Request {
    LOGIN,
    PLAYERNUMBER,
    DIFFICULT,
    START_GAME,

    PLAY_ASSISTANT_CARD,
    MOVE_STUDENT,
    MOVE_STUDENT_DINING,
    MOVE_STUDENT_ISLAND,
    MOVE_MOTHER_NATURE,
    CHOOSE_CLOUD,
    PLAY_CHARACTER_CARD,

    START_PLANNING,
    START_ACTION,
    WIN,
    LOSE,
    MODEL,
    STRING,
}
