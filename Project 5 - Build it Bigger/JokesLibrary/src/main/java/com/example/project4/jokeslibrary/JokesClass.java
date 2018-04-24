package com.example.project4.jokeslibrary;

import java.util.Random;

public class JokesClass {

    public String tellLameJoke() {

        String[] jokes = {"There are 10 kinds of people in world, " +
                "one who understand binary and one who don't",

                "At a party for functions, " +
                "x is at the bar looking despondent. " +
                "The barman says, Why don't you go and integrate? " +
                "To which x replies, It wouldn't make any difference.",

                "They just found the gene for shyness. " +
                "They would have found it earlier, but it was hiding behind two other genes.",

                "A statistician gave birth to twins, but only had one of them baptized. " +
                "She kept the other as a control."
        };

        int rnd = new Random().nextInt(jokes.length);

        return jokes[rnd];
    }
}
