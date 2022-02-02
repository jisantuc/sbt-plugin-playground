with import <nixpkgs> { };

{ pkgs ? import <nixpkgs> { }, jdk ? pkgs.jdk8 }:
let
  jdkOverrideSbt = sbt.override { jre = jdk; };
in
pkgs.mkShell {
  name = "sbt-exercise";
  buildInputs = [
    jdkOverrideSbt
    jdk
  ];
}
