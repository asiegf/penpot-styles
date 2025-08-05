
# penpot-styles

Is a command line utility that will generate the `design-system` or `theming` css file from a penpot file.

> [!NOTE]  
> This project is just beginning, but feel free to report an issue or suggest a feature


The purpose is to keep the design as the source of truth for anything related to the global styles of the app.

So changes in the design can be applied at once.

## Usage
### Install

This tool uses `clojure` (similar to what penpot uses `clojurescript`).

You will need Java installed.

[Installing clojure](https://clojure.org/guides/install_clojure)


### Executing

You will need the penpot [access-token](https://help.penpot.app/technical-guide/integration/#access-tokens), and the file-id of the project you want to generate the CSS.

```bash
export PENPOT_ACCESS_TOKEN=<access-token>
export PENPOT_FILE_ID=<file-id>
```
 
Running will look like this

```bash
clojure -M -m penpot-styles.entry -t $PENPOT_TOKEN -f $PENPOT_FILE_ID
``` 


Manual is available on with `-h`

```bash
clojure -M -m penpot-styles.entry -h
```

## Alternative

Working in JS files, you may want to consider a more official tool [penpot-export](https://github.com/penpot/penpot-export).


## Changelog

### 0.0.1 - 2025-08-05

- minimal publishable version
  - supporting colors and typographies
  - output can be file
  - flag to minify css



<!-- ## Next features -->

<!-- | supporting basic `btn` component | -->
<!-- | supporting `ico` component       | -->
<!-- | extending CSS properties         | -->
