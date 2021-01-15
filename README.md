# hmpps-reference-data

🧑‍🔬 This is an experimental project: it may change.

## Intent

Store reference data as flat files with the following characteristics:

- each entry has a unique ID
- entries are not deleted (instead, add a `start_date`/`end_date` column pair)
- data structure changes are backwards-compatible in the same file
