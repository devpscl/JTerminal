import datetime

source_file = "keys.csv"
target_file = "src/hash_input_translator.cpp"

cpp_pattern = """#include "../include/terminput.h"
//#############################################
//
//  Auto-Generated by keygen.py at %date%
//  
//#############################################


bool jterminal::translateInput(jterminal::InputEvent *event, const uint64_t& hash) {
  switch(hash) {
%s
    default:
      return false;
  }
  return true;
}
"""


class KeyEntry:
    def __init__(self, keycode, state, hash_codes: list[int]):
        self.keycode = keycode
        self.state = state
        self.hash_codes = hash_codes


def hash_key(numbers: list[int]):
    hash_value = 0
    for idx in range(len(numbers)):
        hash_value = 31 * hash_value + numbers[idx]
    return hash_value


def hex_bytes_to_number_list(value: str):
    r = range(int(len(value) / 2))
    numbers = []
    numbers.extend(r)
    for idx in r:
        hex_byte = value[idx * 2] + value[idx * 2 + 1]
        numbers[idx] = int(hex_byte, 16)
    return numbers


def parse_entry(line: str):
    parts = line.split(",")
    keycode = parts[0]
    state = parts[1]
    hash_code_arr = []
    for i in range(2, len(parts)):
        hash_value_hex = parts[i]
        nums = hex_bytes_to_number_list(hash_value_hex)
        hash_code_arr.append(hash_key(nums))

    return KeyEntry(keycode, state, hash_code_arr)


def read_file(file_name: str):
    file = open(file_name, "r")
    content = file.read()
    file.close()
    return content.split("\n")


def get_pattern():
    current_date = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
    return cpp_pattern.replace("%date%", str(current_date))


def generate(file_name: str, entries: list[KeyEntry]):
    out = open(file_name, "w")
    pattern = get_pattern()
    result = pattern.index("%s")
    out.write(pattern[0:result])
    for entry in entries:
        for hash_code in entry.hash_codes:
            if hash_code == 0:
                continue
            out.write("    case " + str(hash_code) + ":\n")
        out.write("      event->keyboard.key = " + entry.keycode + ";\n")
        out.write("      event->keyboard.state = KS_" + entry.state + ";\n")
        out.write("      break;\n")
    out.write(pattern[result + 2:len(pattern)])
    out.flush()


lines = read_file(source_file)
entries = []
for idx in range(1, len(lines)):
    entry_str = lines[idx]
    if len(entry_str) == 0 or entry_str.startswith('#'):
        continue
    e = parse_entry(entry_str)
    entries.append(e)

generate(target_file, entries)