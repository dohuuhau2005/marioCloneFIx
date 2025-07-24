package Com.pack.Mario.Model;

import java.util.ArrayList;
import java.util.List;

public class Validation {
    List<String> errors;

    public Validation() {

    }

    public boolean ExistUser(User user) {
        if (new UserDao().CheckUser(user.getUsername(), user.getEmail()) == true)
            return true;
        return false;
    }

    public List<String> Validate(User user) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        errors = new ArrayList<String>();
        boolean existedEmail = false;
        if (user.getEmail() == null || user.getEmail().equals("")) {
            errors.add("Email is required and cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            errors.add("Password is required and cannot be empty");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            errors.add("Username is required and cannot be empty");
        }
        if (user.getPassword().length() < 6) {
            errors.add("Password must be at least 8 characters");
        }
        if (currentYear - user.getDobYear() < 4) {
            errors.add("Birthday must be at least 4 years old");
        }
        if (!user.getEmail().contains("@")) {
            errors.add("Invalid email");
        }
        if (user.getDobDay() == 0 || user.getDobMonth() == 0 || user.getDobYear() == 0) {
            errors.add("Please select Date of birth");
        }
        return errors;
    }
}
