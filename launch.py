from fastapi import FastAPI
from pydantic import BaseModel
import subprocess

app = FastAPI()

class Msg(BaseModel):
    messages: list

@app.post("/v1/chat/completions")
def chat(msg: Msg):
    prompt = msg.messages[-1]["content"]

    result = subprocess.run(
        [
            "python3", "-m", "mlx_lm.generate",
            "--model", "mlx-community/Qwen2.5-Coder-7B-Instruct",
            "--prompt", prompt,
            "--max-tokens", "512"
        ],
        capture_output=True,
        text=True
    )

    return {
        "choices": [
            {
                "message": {
                    "content": result.stdout
                }
            }
        ]
    }