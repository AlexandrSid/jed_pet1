Хотелось сделать хорошо, чтобы и сообщение прошло через кафку, и композом это всё собиралось вместе и сообщение в телегу админам рассылалось.

Но увы, кафка работает из докера, а приложение только локально (или из композа, но без кафки). Собрать всё приложение одним докер-композ файлом не удалось.

Для запуска кафки надо запустить

_docker-compose -f ./kafka_conf/docker-compose.yml up -d_

из корневой папки проекта, после чего запустить Application.class

Телеграм бот также не в состоянии самостоятельно отсылать сообщения, нужно делать группу информационной рассылки, или уведомление по имейлу