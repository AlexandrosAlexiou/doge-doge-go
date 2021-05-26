#!/usr/bin/env python3

import requests
from bs4 import BeautifulSoup
import wikipediaapi
import json
from urllib.parse import unquote

page_url = "https://en.wikipedia.org/w/index.php?title=Special:Search&limit=500&offset=0&ns0=1&search=covid+covid+intitle%3Acovid&advancedSearch-current={%22fields%22:{%22intitle%22:%22covid%22,%22plain%22:[%22covid%22]}}"
base_url = "https://en.wikipedia.org"

headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36'}

response = requests.get(url=page_url, headers=headers, timeout=10000)
soup = BeautifulSoup(response.content, "html.parser")
wiki_wiki = wikipediaapi.Wikipedia('en')

divs = soup.find_all("div", {"class": "mw-search-result-heading"})

documents = []
for div in divs:
    a = div.findChildren("a", recursive=False)
    article_url = a[0]['href']
    article_title = unquote(article_url[6:])
    page_ref = wiki_wiki.page(article_title)
    print(article_title)
    title = page_ref.title.replace("_", " ")
    text = page_ref.text
    document = {
        "url": f"{base_url}{article_url}",
        "title": title,
        "text": text
    }
    documents.append(document)


with open("corpus.json", "w") as f:
    json.dump(documents, f)

print(f"Scraped: {len(documents)} documents")
