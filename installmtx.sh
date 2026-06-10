#!/bin/bash

set -e

echo "🚀 Setup AI Coding Agent (Mac M4 Max)"

# -------------------------
# 1. Homebrew
# -------------------------
if ! command -v brew &> /dev/null; then
  echo "📦 Installing Homebrew..."
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

eval "$(/opt/homebrew/bin/brew shellenv)"

# -------------------------
# 2. Tools
# -------------------------
echo "📦 Installing base tools..."
brew install git python node wget curl

# -------------------------
# 3. Python env
# -------------------------
echo "🐍 Setting Python env..."
python3 -m venv ~/.ai-agent-env
source ~/.ai-agent-env/bin/activate

pip install --upgrade pip

# -------------------------
# 4. MLX ecosystem
# -------------------------
echo "🧠 Installing MLX stack..."
pip install mlx mlx-lm

# -------------------------
# 5. Model runner (OpenAI compatible server)
# -------------------------
echo "⚙️ Installing MLX server..."
pip install fastapi uvicorn pydantic

cat << 'EOF' > ~/mlx_server.py
from fastapi import FastAPI
import subprocess

app = FastAPI()

@app.post("/v1/chat/completions")
def chat(payload: dict):
    prompt = payload["messages"][-1]["content"]

    result = subprocess.run(
        ["python3", "-m", "mlx_lm.generate",
         "--model", "Qwen/Qwen2.5-Coder-7B-Instruct",
         "--prompt", prompt,
         "--max-tokens", "1024"],
        capture_output=True,
        text=True
    )

    return {
        "choices": [{
            "message": {
                "content": result.stdout
            }
        }]
    }
EOF

# -------------------------
# 6. VS Code Continue
# -------------------------
echo "🧩 Installing Continue extension..."
code --install-extension continue.continue || true

# -------------------------
# 7. Continue config
# -------------------------
mkdir -p ~/.continue

cat << 'EOF' > ~/.continue/config.json
{
  "models": [
    {
      "title": "Local Qwen MLX",
      "provider": "openai",
      "model": "qwen-local",
      "apiBase": "http://localhost:8000/v1",
      "apiKey": "local"
    }
  ]
}
EOF

# -------------------------
# 8. Done
# -------------------------
echo "✅ Installation terminée"

echo ""
echo "👉 Étapes finales :"
echo "1. source ~/.ai-agent-env/bin/activate"
echo "2. python ~/mlx_server.py"
echo "3. Ouvre VS Code et utilise Continue"