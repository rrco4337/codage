#!/bin/bash
# ─────────────────────────────────────────────────────────────
#  build.sh  —  Compiler et lancer l'application
#
#  Usage :
#    ./build.sh          →  compiler + lancer
#    ./build.sh compile  →  compiler seulement
#    ./build.sh run      →  lancer seulement (sans recompiler)
#    ./build.sh clean    →  supprimer tous les .class dans target/
# ─────────────────────────────────────────────────────────────

SRC="algorithmes/*.java ui/*.java ConversionNormale.java"
OUT="target"
MAIN="ConversionNormale"

compile() {
    echo "📦 Compilation → $OUT/"
    mkdir -p "$OUT"
    javac -d "$OUT" $SRC
    if [ $? -eq 0 ]; then
        echo "✅ Compilation réussie."
    else
        echo "❌ Erreur de compilation."
        exit 1
    fi
}

run() {
    echo "🚀 Lancement..."
    java -cp "$OUT" "$MAIN"
}

clean() {
    echo "🗑  Nettoyage de $OUT/"
    rm -rf "$OUT"
    echo "✅ Dossier $OUT/ supprimé."
}

case "$1" in
    compile) compile ;;
    run)     run ;;
    clean)   clean ;;
    *)       compile && run ;;
esac
