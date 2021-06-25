package game.racingcar;

import java.util.*;
import java.util.stream.Collectors;

public class CarRacingService {
    public static final int MAX_CAR_NAME_COUNT = 5;
    public static final int MAX_GAME_COUNT = 10;
    public static final int INIT_SCORE = 1;
    public static final int FORWARD_SCORE = 1;
    public static final int STOP_POINT = 3;
    public static final String CAR_NAME_DELIMITER = ",";
    public static final String RACE_RESULT_DELIMITER = "-";

    public void racingGame(String[] carNames, int gameCount) {
        System.out.println("실행결과");
        Race race = initRace(carNames, gameCount);
        racing(race);
        printWinners(getWinners(race.getCars()));
    }

    public boolean isValidCarNames(String[] carNames) {
        return Arrays.stream(carNames).noneMatch(this::isNotValidCarName);
    }

    private boolean isNotValidCarName(String carName) {
        return carName.length() == 0 || carName.length() > MAX_CAR_NAME_COUNT;
    }

    public String[] getCarNames(String input) {
        return Arrays.stream(input.split(CAR_NAME_DELIMITER)).map(String::trim).toArray(String[]::new);
    }

    public boolean isValidGameCount(String input) {
        try {
            Integer.parseInt(input);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Race initRace(String[] carNames, int gameCount) {
        List<Car> carList = Arrays.stream(carNames)
                .map(carName -> new Car(carName, INIT_SCORE))
                .collect(Collectors.toList());
        return new Race(gameCount, carList);
    }

    public void racing(Race race) {
        for (int i = 0; i < race.getGameCount(); i++) {
            race.getCars().forEach(this::raceScore);
            System.out.println();
        }
    }

    public boolean isForward(int randomNumber) {
        return randomNumber > STOP_POINT;
    }

    private void raceScore(Car car) {
        printScore(car);
        int randomNumber = (int) (Math.random() * 10);
        if (isForward(randomNumber)) {
            car.addScore(FORWARD_SCORE);
        }
    }

    public void printScore(Car car) {
        String raceResult = String.join("", Collections.nCopies(car.getScore(), RACE_RESULT_DELIMITER));
        String printFormat = String.format("%s : %s", car.getName(), raceResult);
        System.out.println(printFormat);
    }

    public String[] getWinners(List<Car> cars) {
        int max = cars.stream().max(Comparator.comparing(Car::getScore)).get().getScore();

        return cars.stream().filter(car -> car.getScore() == max).map(Car::getName).toArray(String[]::new);
    }

    public void printWinners(String[] winners) {
        String winner = String.join(",", winners);
        System.out.println(winner + "가 최종 우승했습니다.");
    }
}
