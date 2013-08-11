package packagepath

import packagepath.auth.User


class Profile {
    String name
    String email

    static belongsTo = [user: User]

    static constraints = {
        name blank: false
        email blank: false
    }
}
