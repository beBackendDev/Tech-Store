import csv

from pathlib import Path


# ============================================================
# PATH
# ============================================================

INPUT_FILE = Path("../raw/laptop.csv")

OUTPUT_PRODUCTS = Path(
    "../processed/products.csv"
)

OUTPUT_LAPTOP_SPECS = Path(
    "../processed/laptop_specifications.csv"
)


# ============================================================
# DEFAULT VALUES
# ============================================================

DEFAULT_CATEGORY = "LAPTOP"

DEFAULT_STOCK = 10

DEFAULT_RATING = "0.0"

DEFAULT_REVIEW_COUNT = 0

DEFAULT_IS_NEW = False

DEFAULT_ACTIVE = True


# ============================================================
# HELPER FUNCTIONS
# ============================================================

def clean_string(value):
    """
    Remove leading/trailing whitespace.

    None -> ""
    """

    if value is None:
        return ""

    return str(value).strip()


def clean_integer(value, default=0):
    """
    Convert value to Integer.

    Examples:
        "8"      -> 8
        "8.0"    -> 8
        ""       -> default
    """

    value = clean_string(value)

    if not value:
        return default

    try:

        return int(float(value))

    except (ValueError, TypeError):

        return default


def clean_decimal(value, default="0.0"):
    """
    Convert value to decimal string.

    Examples:
        "15.6"   -> "15.6"
        "33921"  -> "33921.0"
    """

    value = clean_string(value)

    if not value:
        return default

    try:

        return str(float(value))

    except (ValueError, TypeError):

        return default


def clean_resolution(value):
    """
    Normalize screen resolution.

    Example:

        1920 x 1080

    becomes:

        1920x1080
    """

    value = clean_string(value)

    return value.replace(
        " ",
        ""
    )


def generate_external_id(index):
    """
    Generate stable external product ID.

    Example:
        LAP-000001
        LAP-000002
        LAP-000003
    """

    return f"LAP-{index:06d}"


# ============================================================
# TRANSFORM
# ============================================================

def transform_products():

    # --------------------------------------------------------
    # CREATE OUTPUT DIRECTORY
    # --------------------------------------------------------

    OUTPUT_PRODUCTS.parent.mkdir(
        parents=True,
        exist_ok=True
    )


    # --------------------------------------------------------
    # OPEN RAW CSV
    # --------------------------------------------------------

    with open(
        INPUT_FILE,
        "r",
        encoding="utf-8",
        newline=""
    ) as input_file:

        reader = csv.DictReader(
            input_file
        )


        # ----------------------------------------------------
        # PRODUCTS CSV
        # ----------------------------------------------------

        with open(
            OUTPUT_PRODUCTS,
            "w",
            encoding="utf-8",
            newline=""
        ) as products_file:

            products_writer = csv.DictWriter(
                products_file,

                fieldnames=[
                    "external_id",
                    "name",
                    "description",
                    "category",
                    "price",
                    "old_price",
                    "stock",
                    "image",
                    "rating",
                    "review_count",
                    "is_new",
                    "active"
                ]
            )

            products_writer.writeheader()


            # ------------------------------------------------
            # LAPTOP SPECIFICATIONS CSV
            # ------------------------------------------------

            with open(
                OUTPUT_LAPTOP_SPECS,
                "w",
                encoding="utf-8",
                newline=""
            ) as specs_file:

                specs_writer = csv.DictWriter(
                    specs_file,

                    fieldnames=[
                        "product_external_id",
                        "brand",
                        "processor",
                        "ram",
                        "ssd",
                        "hard_disk",
                        "operating_system",
                        "graphics",
                        "screen_size",
                        "resolution"
                    ]
                )

                specs_writer.writeheader()


                # ============================================
                # TRANSFORM EACH ROW
                # ============================================

                for index, row in enumerate(
                    reader,
                    start=1
                ):

                    # ----------------------------------------
                    # EXTERNAL ID
                    # ----------------------------------------

                    external_id = generate_external_id(
                        index
                    )


                    # ----------------------------------------
                    # PRODUCT
                    # ----------------------------------------

                    name = clean_string(
                        row.get("model_name")
                    )


                    price = clean_decimal(
                        row.get("price")
                    )


                    description = (
                        f"{name} laptop"
                        if name
                        else "Laptop"
                    )


                    products_writer.writerow({

                        "external_id":
                            external_id,

                        "name":
                            name,

                        "description":
                            description,

                        "category":
                            DEFAULT_CATEGORY,

                        "price":
                            price,

                        # Dataset does not provide
                        # original/old price.
                        "old_price":
                            "",

                        "stock":
                            DEFAULT_STOCK,

                        # Image will be handled separately
                        # through Google Cloud Storage.
                        "image":
                            "",

                        "rating":
                            DEFAULT_RATING,

                        "review_count":
                            DEFAULT_REVIEW_COUNT,

                        "is_new":
                            DEFAULT_IS_NEW,

                        "active":
                            DEFAULT_ACTIVE

                    })


                    # ----------------------------------------
                    # LAPTOP SPECIFICATION
                    # ----------------------------------------

                    specs_writer.writerow({

                        "product_external_id":
                            external_id,

                        "brand":
                            clean_string(
                                row.get("brand")
                            ),

                        "processor":
                            clean_string(
                                row.get(
                                    "processor_name"
                                )
                            ),

                        "ram":
                            clean_integer(
                                row.get(
                                    "ram(GB)"
                                )
                            ),

                        "ssd":
                            clean_integer(
                                row.get(
                                    "ssd(GB)"
                                )
                            ),

                        "hard_disk":
                            clean_integer(
                                row.get(
                                    "Hard Disk(GB)"
                                )
                            ),

                        "operating_system":
                            clean_string(
                                row.get(
                                    "Operating System"
                                )
                            ),

                        "graphics":
                            clean_string(
                                row.get(
                                    "graphics"
                                )
                            ),

                        "screen_size":
                            clean_decimal(
                                row.get(
                                    "screen_size(inches)"
                                )
                            ),

                        "resolution":
                            clean_resolution(
                                row.get(
                                    "resolution (pixels)"
                                )
                            )

                    })


    print(
        "========================================"
    )

    print(
        "Products transformed successfully."
    )

    print(
        f"Products CSV: {OUTPUT_PRODUCTS}"
    )

    print(
        f"Specifications CSV: {OUTPUT_LAPTOP_SPECS}"
    )

    print(
        "========================================"
    )


# ============================================================
# ENTRY POINT
# ============================================================

if __name__ == "__main__":

    transform_products()
