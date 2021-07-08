-- remove long login text as button too small
delete from message where [key] = 'label.login' and language = 'fr'