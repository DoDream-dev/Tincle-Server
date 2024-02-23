package tinqle.tinqleServer.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.exception.AccountException;


@AllArgsConstructor
@Getter
public enum Status {
    SMILE,
    HAPPY,
    SAD,
    MAD,
    EXHAUSTED,
    CHICKEN,
    ALCOHOL,
    COFFEE,
    READ,
    WORK,
    MEAL,
    MOVIE,
    SLEEP,
    TRAVEL,
    STUDY,
    WALK,
    MOVE,
    DANCE,
    TRANSPORT,
    EAT,
    WORKOUT,
    PANIC,
    EXCITED,
    SICK,
    VACATION,
    DATE,
    COMPUTER,
    CAFE,
    MUSIC,
    BIRTHDAY;


    public static Status toEnum(String status) {
        return switch (status.toUpperCase()) {
            case "SMILE" -> SMILE;
            case "HAPPY" -> HAPPY;
            case "SAD" -> SAD;
            case "MAD" -> MAD;
            case "EXHAUSTED" -> EXHAUSTED;
            case "CHICKEN" -> CHICKEN;
            case "ALCOHOL" -> ALCOHOL;
            case "COFFEE" -> COFFEE;
            case "READ" -> READ;
            case "WORK" -> WORK;
            case "MEAL" -> MEAL;
            case "MOVIE" -> MOVIE;
            case "SLEEP" -> SLEEP;
            case "TRAVEL" -> TRAVEL;
            case "STUDY" -> STUDY;
            case "WALK" -> WALK;
            case "MOVE" -> MOVE;
            case "DANCE" -> DANCE;
            case "TRANSPORT" -> TRANSPORT;
            case "EAT" -> EAT;
            case "WORKOUT" -> WORKOUT;
            case "PANIC" -> PANIC;
            case "EXCITED" -> EXCITED;
            case "SICK" -> SICK;
            case "VACATION" -> VACATION;
            case "DATE" -> DATE;
            case "COMPUTER" -> COMPUTER;
            case "CAFE" -> CAFE;
            case "MUSIC" -> MUSIC;
            case "BIRTHDAY" -> BIRTHDAY;
            default -> throw new AccountException(StatusCode.NOT_FOUND_STATUS);
        };
    }
}
