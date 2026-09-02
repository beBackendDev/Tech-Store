import requests
import json
from pathlib import Path


API_URL = "https://dummyjson.com/products?limit=200"

OUTPUT_FILE = Path("../raw/products.json")


def fetch_products():

    response = requests.get(API_URL, timeout=10)

    response.raise_for_status()

    data = response.json()

    OUTPUT_FILE.parent.mkdir(
        parents=True,
        exist_ok=True
    )

    with open(
        OUTPUT_FILE,
        "w",
        encoding="utf-8"
    ) as file:

        json.dump(
            data,
            file,
            ensure_ascii=False,
            indent=2
        )

    print(
        f"Fetched {len(data['products'])} products"
    )


if __name__ == "__main__":
    fetch_products()