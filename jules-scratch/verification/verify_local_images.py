from playwright.sync_api import sync_playwright
import os

def run():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        current_path = os.path.abspath(os.getcwd())
        rutina_path = f"file://{os.path.join(current_path, 'MyGymJavaWeb/web/rutina.jsp')}"

        page.goto(rutina_path)
        page.screenshot(path="jules-scratch/verification/06_rutina_local_image.png")

        browser.close()

if __name__ == "__main__":
    run()
