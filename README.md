# hmpps-reference-data

[![Docker badge](https://img.shields.io/badge/docker-image-2496ED?style=flat&logo=docker)](https://github.com/ministryofjustice/hmpps-reference-data/pkgs/container/hmpps-reference-data)

🧑‍🔬 This is an experimental project: it may change.

## Intent

Store reference data as flat files with the following characteristics:

- each entry has a unique ID
- the unique ID is in the _first_ column
- entries are not deleted (instead, add a `start_date`/`end_date` column pair)
- data structure changes are backwards-compatible in the same file:
    - cannot remove existing columns
    - cannot rename existing columns
    - can add new columns

## Where are the files?

The reference data files are CSVs in the [`./registers/`](registers) directory.

These are converted to JSON files during the [build process](.github/workflows/publish.yml) and published. For example:
```
docker run --rm -p 8000:8000 ghcr.io/ministryofjustice/hmpps-reference-data:main
```
Then visit

- http://localhost:8000/probation-regions-v0.json
- http://localhost:8000/probation-regions-v0.csv

to access the probation region reference data.

## Checking backwards compatibility

To build, test, assemble the project:
```
$ ./gradlew build
<snip>
BUILD SUCCESSFUL in 2s
10 actionable tasks: 10 executed
```

To run the tool:
```
$ ./gradlew run

> Task :app:run
🔍 Checking if register files are backwards compatible with 'origin/main'...
registers/nomis-ethnicity.csv: ✅ pass
registers/nomis-gender.csv: ✅ pass
registers/nomis-locations.csv: ✅ pass
registers/nomis-suffix.csv: ✅ pass
registers/nomis-titles.csv: ✅ pass
registers/offences.csv: ✅ pass
registers/pcc-regions-for-probation-v0.csv: ✅ pass
registers/probation-delivery-units-v0.csv: ✅ pass
registers/probation-offices-v0.csv: ✅ pass
registers/probation-regions-v0.csv: ✅ pass

BUILD SUCCESSFUL in 1s
2 actionable tasks: 1 executed, 1 up-to-date
```
