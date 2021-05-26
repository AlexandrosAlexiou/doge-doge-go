#!/usr/bin/env python3
import json

input_file = open ('data.json')
json_array = json.load(input_file)
final_data = []

for item in json_array:
    store_details = {"url":None, "title":None, "text":None}
    store_details['url'] = item['url']
    store_details['title'] = item['title'].replace("_", " ")
    store_details['text'] = item['text']
    final_data.append(store_details)


with open("corpus.json", "w") as f:
    json.dump(final_data, f)
