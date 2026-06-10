#!/bin/bash

set -e

BASE="/Users/sd/ai-creators"

echo "🚀 Setup AI Agent in $BASE"

# -------------------------
# 1. Create base folder
# -------------------------
mkdir -p "$BASE"

# -------------------------
# 2. Install Homebrew if needed
# -------------------------
if ! command -v brew &> /dev/null; then
  echo "📦 Installing Homebrew..."
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

eval "$(/opt/homebrew/bin/brew shellenv)"

# -------------------------
# 3. Install dependencies
# -------------------------
echo "📦 Installing dependencies..."
brew install git curl wget python@3.12 node

PYTHON_BIN=$(brew --prefix python@3.12)/bin/python3.12

# -------------------------
# 4. Create virtual env INSIDE ai-creators
# -------------------------
echo "🐍 Creating virtual env..."
$PYTHON_BIN -m venv "$BASE/venv"

source "$BASE/venv/bin/activate"

pip install --upgrade pip

# -------------------------
# 5. Install AI stack
# -------------------------
echo "🧠 Installing MLX stack..."
pip install fastapi uvicorn mlx mlx-lm pydantic

# -------------------------
# 6. Create server in ai-creators
# -------------------------
cat << 'EOF' > "$BASE/server.py"
from fastapi import FastAPI
from pydantic import BaseModel
import subprocess

app = FastAPI()

class Msg(BaseModel):
    messages: list

MODEL = "mlx-community/Qwen2.5-Coder-7B-Instruct"

@app.post("/v1/chat/completions")
def chat(msg: Msg):
    prompt = msg.messages[-1]["content"]

    result = subprocess.run(
        [
            "python3", "-m", "mlx_lm.generate",
            "--model", MODEL,
            "--prompt", prompt,
            "--max-tokens", "800"
        ],
        capture_output=True,
        text=True
    )

    return {
        "choices": [
            {"message": {"content": result.stdout}}
        ]
    }
EOF

# -------------------------
# 7. Start script in same folder
# -------------------------
cat << 'EOF' > "$BASE/start.sh"
#!/bin/bash

BASE="/Users/sd/ai-creators"

source "$BASE/venv/bin/activate"

echo "🚀 Starting AI server..."

while true; do
  uvicorn server:app --host 0.0.0.0 --port 8000 --reload

  echo "⚠️ Restarting server in 2s..."
  sleep 2
done
EOF

chmod +x "$BASE/start.sh"

# -------------------------
# 8. Done
# -------------------------
echo "✅ DONE"
echo ""
echo "👉 Start server:"
echo "   bash /Users/sd/ai-creators/start.sh"
echo ""
echo "👉 Test:"
echo "   curl http://localhost:8000/v1/chat/completions"