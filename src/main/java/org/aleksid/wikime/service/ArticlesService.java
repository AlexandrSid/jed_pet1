package org.aleksid.wikime.service;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.Tag;
import org.aleksid.wikime.repository.ArticlesRepository;
import org.aleksid.wikime.util.ArticleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticlesService {

    //make it singleton
    private static final ArticlesService service = new ArticlesService();

    private ArticlesService() {
    }

    public static ArticlesService getInstance() {
        return service;
    }
    @Autowired
    private ArticlesRepository repository;


    public List<Article> getArticlesContainingTags() {
        return repository.getAll();
    }

    public List<Article> getAllArticles() {
        return repository.getAll();
    }

    public List<Article> getArticlesContainingTags(List<Tag> tags) {
        if (tags.isEmpty()) return getArticlesContainingTags();
        return sortByTagsOrder(tags, repository.getFilteredByTags(tags));
    }

    public Article getArticleByID(int id) {
        if (id == 0) {
            return ArticleUtil.getEmptyArticle();
        }
        return repository.getById(id);
    }

    public boolean deleteArticleByID(int id) {
        return repository.delete(id);
    }

    public Article constructAndReturn(int id, String header, String tags, String text) {
        return ArticleUtil.constructAndReturn(id, header, tags, text);
    }

    public Article update(Article article) {
        if (
                article.getHeader().isEmpty() &&
                article.getTags().isEmpty() &&
                article.getParagraphs().isEmpty()
        ) {
            repository.delete(article.getId());
            return new Article();
        }
        return repository.update(article);
    }

    public Article addArticle(Article article) {
        return repository.add(article);
    }


    public List<Tag> createTagsFromRequest(String articleTags) {
        List tags = ((articleTags == null) || (articleTags.isEmpty()))
                ? Collections.emptyList()
                : Arrays.stream(articleTags.split(", ")).distinct().map(Tag::new).collect(Collectors.toList());
        return tags;
    }

    private List<Article> sortByTagsOrder(List<Tag> tags, Collection<Article> articles) {
        //внутренний класс для сортировки
        class ArticleContainer {
            Article article;
            int rating;

            public ArticleContainer(Article article) {
                this.article = article;
                this.rating = 0;
            }

            public Article getArticle() {
                return article;
            }

            public void setRating(int rating) {
                this.rating = rating;
            }
        }
        //каждому тегу в запрашиваемой последовательности назначается рейтинг, пришёл первым - получил меньше(1,2,4,8...)
        Map<String, Integer> tagRating = new HashMap<>();
        int r = 128;//первые 7 тегов, остальные не будут влиять на порядок
        for (Tag tag : tags) {
            tagRating.put(tag.getTag(), r /= 2);
        }

        //заполняется лист для сортировки...
        List<ArticleContainer> sortable = articles.stream().distinct().map(ArticleContainer::new).collect(Collectors.toList());
        //...рассчитывается рейтинг каждой подходящей статьи
        sortable.forEach(container -> container.setRating(container
                .getArticle()
                .getTags()
                .stream()
                .map(tag -> tagRating.getOrDefault(tag.getTag(), 0))
                .reduce(0, Integer::sum))
        );

        //список сортируется
        Collections.sort(sortable, new Comparator<ArticleContainer>() {
            @Override
            public int compare(ArticleContainer o1, ArticleContainer o2) {
                return o2.rating - o1.rating;
            }
        });

        return sortable.stream().map(ArticleContainer::getArticle).collect(Collectors.toList());
    }
}
