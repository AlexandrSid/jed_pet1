package model;

import repository.ArticlesRepository;

import java.util.Arrays;
import java.util.HashSet;

public class Main {
    //не уверен, что данный класс лучше всего подходит для этого,
    // но чёт ничего более подходящего в проекте пока нет
    public static final void initializeRepo(ArticlesRepository repository) {
        repository.add(new Article(
                new HashSet<aTag>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag2"),
                        new aTag("tag4"))
                ),
                "Article 1",
                Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
        ));

        repository.add(new Article(
                new HashSet<aTag>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag3"),
                        new aTag("tag4"))
                ),
                "Article 2",
                Arrays.asList("Word1", "Word2", "words 3", "Words 4")
        ));

    }
}
