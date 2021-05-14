# DogeDogeGo Spring Boot API

## API endpoints

These endpoints allow you to retrieve data from the corpus based on a search term.

## GET

- ## Keyword Search
```
GET /api/v1/query?q=${term}
```

e.g. Search for keyword `women`
```
GET /api/v1/query?q=women
```
**Content example**

```json
{
  "url": "https://en.wikipedia.org/wiki/COVID-19_in_pregnancy",
  "title": "COVID-19_in_pregnancy",
  "text": "of reliable data. If there is increased risk to pregnant <b><font color='black'>women</font></b> and fetuses, so far it has not been readily detectable.\nPredictions based on similar infections such as SARS and MERS suggest that pregnant <b><font color='black'>women</font></b>... characteristics of COVID-19 pneumonia in pregnant <b><font color='black'>women</font></b> were similar to those reported from non... factors for pregnant <b><font color='black'>women</font></b> as they do for non-pregnant women.From the limited data available, vertical... <b><font color='black'>women</font></b>\nIn May 2020, the Royal College of Obstetricians and Gynaecologists (RCOG) and the Royal College... pregnant <b><font color='black'>women</font></b> and their babies. This study showed that 4.9 pregnant <b><font color='black'>women</font></b> per 1000 were admitted... support earlier suggestions that pregnant <b><font color='black'>women</font></b> are not at greater risk of severe illness than non-pregnant <b><font color='black'>women</font></b>. Similar risk factors also apply: <b><font color='black'>women</font></b> in the study were more likely to be admitted... or high blood pressure. Five <b><font color='black'>women</font></b> died but it is not yet clear whether the virus was the cause of death. Since the majority of <b><font color='black'>women</font></b> who became severely ill were in their third trimester of pregnancy"
}
```

- ## Spell checking
```
GET /api/v1/suggestions?t=${term}
```
e.g. Suggestions for keyword `chldrn`
```
GET /api/v1/suggestions?t=chldrn
```
**Content example**

```json
{
    "0" : "children",
    "1" : "chlef",
    "2" : "chloe",
    "3" : "chlsd",
    "4" : "chlorate",
    "5" : "chloride",
    "6" : "chlorine",
    "7" : "chlorite",
    "8" : "chlorous",
    "9" : "culdrose"
}
```