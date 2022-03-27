package hu.uni.ekcu.Nimeria.exercise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExerciseNotFoundException extends RuntimeException{

    public ExerciseNotFoundException(String msg) {
        super(msg);
    }
}
