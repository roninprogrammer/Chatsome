package com.dananaka.chatsome.user.data_model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vicknesh on 27/12/16.
 */

public class Users {

    private final List<User> users;

    public Users(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void remove(User u) {
        if (users.contains(u)) {
            users.remove(u);
        }
    }

    public int size() {
        return users.size() ;
    }

    public User getUserAt(int position) {
        return users.get(position);
    }


    public Users sortedByName(String fUid) {
        List<User> sortedList = new ArrayList<>(users);
        Collections.sort(sortedList,byName());
        List<User> filterList  = filterById((ArrayList) sortedList,fUid );
        return new Users(filterList);
    }

    // Remove specific string holder list item from ArrayList and return ArrayList.
    public List<User> filterById(ArrayList list, String uid) {
        Iterator<User> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getUid().contains(uid))
                it.remove();
        }
        return list;
    }

    private static Comparator<? super User> byName() {
        return new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        };
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Users users1 = (Users) o;

        return users.equals(users1.users);

    }

    @Override
    public int hashCode() {
        return users.hashCode();
    }

    @Override
    public String toString() {
        return "Users{" +
                "users=" + users +
                '}';
    }
}