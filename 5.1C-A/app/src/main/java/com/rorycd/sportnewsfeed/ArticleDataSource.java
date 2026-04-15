package com.rorycd.sportnewsfeed;

import java.util.Arrays;
import java.util.List;

public class ArticleDataSource {

    public static List<Article> getArticles() {
        return Arrays.asList(
            new Article(
                1,
                R.drawable.afl_1,
                "Grand final woes for Collingwood",
                "Football",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas pharetra nisl quis leo sollicitudin, quis condimentum velit iaculis. Aenean ipsum massa, vestibulum sit amet porttitor nec, commodo in orci. Vestibulum id fermentum eros. Nullam ut dapibus massa. In fringilla lacus vel finibus egestas. Vestibulum tempus suscipit congue. Vestibulum tempus fringilla augue sed tincidunt. Aliquam erat volutpat. Ut id ipsum eu dolor lacinia facilisis quis a arcu. Pellentesque vel lacus nulla. Fusce metus ante, lacinia eu porttitor et, accumsan vitae quam. Fusce luctus enim enim, eget pharetra odio ullamcorper vel. Maecenas tempus, magna sed dignissim convallis, lorem nisl vehicula ipsum, id elementum lacus nulla quis libero. Maecenas urna dui, convallis sed luctus bibendum, auctor nec erat. Ut maximus hendrerit augue non lobortis. Nam cursus pharetra risus."
            ),
            new Article(
                2,
                R.drawable.quidditch_1,
                "Australia makes the quidditch world cup",
                "Quidditch",
                "Phasellus bibendum dui ut nisi hendrerit, non dignissim nibh porttitor. Sed viverra a urna quis finibus. Vestibulum iaculis justo in sollicitudin dignissim. Nulla vulputate laoreet ante, eget dictum dui mattis eget. Sed sed libero laoreet nunc fringilla dictum. Morbi viverra dignissim enim ac accumsan. In et libero nec leo pretium aliquet. Fusce ornare ante augue, eu rhoncus leo imperdiet in. Nullam pharetra odio erat, at pretium dui ornare sit amet. "
            ),
            new Article(
                3,
                R.drawable.swimming_1,
                "Broken Hill women's swimming team break new record",
                "Swimming",
                "Sed quis egestas nisi, eget venenatis est. Cras molestie maximus convallis. Vivamus cursus tincidunt lorem, sed convallis tellus efficitur sodales. Proin at ipsum id eros scelerisque efficitur at nec ex. Suspendisse efficitur in sapien eget ultricies. Curabitur tristique ipsum in quam efficitur interdum. Nulla sit amet hendrerit elit, a faucibus ex. Ut nec turpis ullamcorper, pellentesque magna vitae, tincidunt tortor. Ut a interdum metus, at ultrices orci. Nullam commodo pharetra magna, vel egestas leo consectetur vel. Maecenas eu accumsan dolor, id hendrerit sem. In hac habitasse platea dictumst."
            ),
            new Article(
                4,
                R.drawable.tennis_1,
                "Novak Djokovic wins yet another grand slam",
                "Tennis",
                "Curabitur vehicula ut neque id accumsan. In ultricies vitae purus vitae malesuada. Nunc id nisi eget massa vulputate posuere. Nulla scelerisque massa nunc, a semper eros venenatis vitae. Pellentesque quis quam luctus lacus accumsan rhoncus. Aliquam hendrerit tellus sed urna facilisis, non dapibus est accumsan. Sed turpis dui, dapibus volutpat dui at, feugiat malesuada ligula. Quisque nunc ante, consequat in lectus eu, molestie rutrum urna. Sed vulputate, felis laoreet finibus ullamcorper, justo magna suscipit mauris, sed sollicitudin enim nisl in neque. Nunc eleifend, massa vel vehicula ultricies, turpis velit blandit massa, at feugiat augue magna vel ligula. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam nec leo bibendum lacus dapibus eleifend."
            ),
            new Article(
                5,
                R.drawable.soccer_1,
                "Riots as Liverpool beat Manchester",
                "Soccer",
                "Ut consectetur bibendum elit rutrum congue. Nunc accumsan sapien dolor, sit amet venenatis libero maximus quis. Curabitur posuere lectus vel commodo dignissim. Proin rhoncus accumsan iaculis. Pellentesque velit velit, mollis at libero vel, laoreet faucibus erat. Curabitur sed sollicitudin nisl. Vivamus pellentesque nunc et lectus volutpat, a dapibus nisl volutpat. Suspendisse gravida odio justo, eu vestibulum nunc lacinia quis. Duis dui eros, lobortis et erat tempor, sagittis dapibus nunc. Sed dapibus metus ante, nec viverra est pretium nec. Proin ac odio elit. Nulla id ornare sem, ut lobortis nisi. Donec eu euismod orci. Vivamus ac diam ex. Integer molestie, orci quis lacinia tempor, orci arcu bibendum purus, a interdum mauris libero vel orci."
            ),
            new Article(
                6, R.drawable.afl_2,
                "Injuries plague Geelong",
                "Football",
                "Donec pharetra ipsum sed aliquam dignissim. Integer blandit, massa non mattis sollicitudin, ante massa tempor orci, vel mollis lacus turpis et arcu. Suspendisse bibendum ante nec tempor rhoncus. Suspendisse dapibus eros ut sem imperdiet mattis. Proin sit amet dignissim erat. Nam sit amet finibus nunc. Maecenas mollis magna scelerisque, accumsan mi sit amet, laoreet nunc. Proin gravida suscipit lorem, quis fermentum risus tincidunt non. Ut semper vestibulum malesuada. Nullam quam purus, convallis gravida augue vitae, vehicula consequat nisi. Maecenas vulputate blandit diam a fringilla. Praesent magna eros, elementum at ullamcorper nec, eleifend non elit. Cras at nisl ultricies, lobortis orci eget, porta enim. Praesent semper metus in ornare placerat. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos."
            ),
            new Article(
                7, R.drawable.afl_3,
                "Gary Ablett statue to be erected in central Geelong",
                "Football",
                "Curabitur tempor mattis lorem vitae placerat. Mauris lorem lacus, facilisis vel nulla vel, congue dictum mi. Integer dignissim mauris est, molestie dictum dolor gravida eget. Proin viverra justo erat, in feugiat est maximus non. Maecenas et massa vitae velit tincidunt venenatis vel in nisl. In vitae ex sem. Morbi nulla nisi, pretium ut viverra ac, porta et erat. In at ante id urna imperdiet viverra. Phasellus posuere in magna et finibus. Sed eu volutpat ex. Morbi suscipit leo a finibus egestas. Mauris facilisis lobortis turpis nec imperdiet. Nullam eu maximus magna."
            ),
            new Article(
                8, R.drawable.afl_4,
                "Is this the end of the iconic red footy?",
                "Football",
                "Nullam auctor finibus egestas. Nam ornare ipsum eget gravida aliquam. Pellentesque blandit lectus tortor, sed blandit lectus elementum ac. In volutpat imperdiet turpis, vel vestibulum turpis tristique in. Vivamus auctor libero eget velit ornare congue. Vivamus semper nunc molestie dignissim facilisis. Pellentesque sollicitudin tincidunt mi a imperdiet. Aliquam commodo metus at risus malesuada, sed pretium nisl tempus. Vestibulum pellentesque convallis egestas. Vestibulum sit amet vulputate metus, ut congue nisl. In hac habitasse platea dictumst."
            ),
            new Article(
                9, R.drawable.soccer_2,
                "New soccer stadium to open in Leopold",
                "Soccer",
                "Nullam auctor finibus egestas. Nam ornare ipsum eget gravida aliquam. Pellentesque blandit lectus tortor, sed blandit lectus elementum ac. In volutpat imperdiet turpis, vel vestibulum turpis tristique in. Vivamus auctor libero eget velit ornare congue. Vivamus semper nunc molestie dignissim facilisis. Pellentesque sollicitudin tincidunt mi a imperdiet. Aliquam commodo metus at risus malesuada, sed pretium nisl tempus. Vestibulum pellentesque convallis egestas. Vestibulum sit amet vulputate metus, ut congue nisl. In hac habitasse platea dictumst."
            )
        );
    }

    public static Article getArticleById(int id) {
        for (Article article : getArticles()) {
            if (article.getId() == id) return article;
        }
        return null;
    }
}
