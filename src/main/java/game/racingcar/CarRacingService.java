package game.racingcar;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CarRacingService {
    public static final int MAX_CAR_COUNT = 5;
    public static final int MAX_GAME_COUNT = 10;
    public static final int INIT_SCORE = 1;
    public static final int FORWARD_POINT = 4;
    public static final int FORWARD_STEP = 1;
    public static final String CAR_NAME_DELIMITER = ",";
    public static final String RACE_RESULT_DELIMITER = "-";

    public void racingGame(String[] carNames, int gameCount) {
        System.out.println("실행결과");
        Race race = initRace(carNames, gameCount);
        racing(race);
        printWinners(getWinners(race.getCars()));
    }

    public boolean checkCarNames(String[] carNames) {
        long count = Arrays.stream(carNames).filter(car -> car.length() > MAX_CAR_COUNT).count();

        return count <= 0;
    }

    public String[] getCarNames(String input) {
        return Arrays.stream(input.split(CAR_NAME_DELIMITER)).map(String::trim).toArray(String[]::new);
    }

    public boolean checkGameCount(int input) {
        return input <= MAX_GAME_COUNT;
    }

    public int getGameCount(String input) {
        if (!Pattern.matches("^[0-9]*$", input)) {
            throw new NumberFormatException("잘못된 입력입니다. 정수를 입력해주세요.");
        }
        return Integer.parseInt(input);
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
        return randomNumber >= FORWARD_POINT;
    }

    private void raceScore(Car car) {
        printScore(car);
        int randomNumber = (int) (Math.random() * 10);
        if (isForward(randomNumber)) {
            car.addScore(FORWARD_STEP);
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