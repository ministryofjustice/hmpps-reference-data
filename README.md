# hmpps-reference-data

🧑‍🔬 This is an experimental project: it may change.

## Intent

Store reference data as flat files with the following characteristics:

- each entry has a unique ID
- entries are not deleted (instead, add a `start_date`/`end_date` column pair)
- data structure changes are backwards-compatible in the same file

## Convert to JSON

```
docker build . --tag=reference-data
docker run --rm reference-data <csvfile> | jq .
```

Note: CSVs are copied into the image. To view "live" files, pipe the file through stdin:

```json
$ docker run --rm --interactive reference-data < ~/Downloads/uk_bank_holidays.csv | tail -n3
{"Name":"Christmas","Bank Holiday":"1","Date":"2020-12-25"},
{"Name":"Boxing Day","Bank Holiday":"1","Date":"2020-12-28"}
]
```
