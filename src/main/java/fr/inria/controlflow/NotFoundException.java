package fr.inria.controlflow;

/**
 * Created by marodrig on 09/11/2015.
 */
public class NotFoundException extends Exception {
    public NotFoundException(String found) {
        super(found);
    }
}
