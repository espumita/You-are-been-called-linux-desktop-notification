#!/bin/bash
mosquitto_sub -t call  |
  while IFS= read -r line
  do
    notify-send 'Call incoming' "$line"
  done