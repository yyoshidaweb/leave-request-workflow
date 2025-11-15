/**
 * コピーボタンの処理
 */
document.addEventListener('DOMContentLoaded', () => {
    const copyButtons = document.querySelectorAll('.copy-btn');

    copyButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetId = button.getAttribute('data-target');
            const textElem = document.getElementById(targetId);

            if (textElem) {
                const text = textElem.textContent.trim();
                navigator.clipboard.writeText(text)
                    .then(() => {
                        // アイコン変更
                        const icon = button.querySelector('i');
                        const originalClass = icon.className; // 元のアイコンを保存

                        icon.className = 'bi bi-check2'; // ✔ に変更

                        // 1.6秒後に元に戻す
                        setTimeout(() => {
                            icon.className = originalClass;
                        }, 1600);
                    })
                    .catch(err => console.error('コピー失敗:', err));
            }
        });
    });
});
