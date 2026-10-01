import os
import glob

def fix_text(text):
    # Intentar revertir la doble codificación de UTF-8 a Windows-1252
    try:
        # text was read as UTF-8. It has 'Ã³' which is actually the bytes of 'ó' in UTF-8 interpreted as latin1.
        return text.encode('windows-1252').decode('utf-8')
    except:
        return text

for filepath in glob.glob('inventario/**/*.java', recursive=True):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    fixed = fix_text(content)
    if fixed != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(fixed)
        print(f'Fixed {filepath}')
