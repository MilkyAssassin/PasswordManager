// @ts-nocheck
// Thank you to @kevmodrome https://svelte.dev/playground/2254c3b9b9ba4eeda05d81d2816f6276?version=5.2.11

function createNotificationStore(timeout) {
    const _notifications: {}[] = $state([]);
    function generateId() {
        return "_" + Math.random().toString(36).substr(2, 9);
    }

    function send(message, type = "default", timeout) {
        const id = generateId();
        _notifications.push({ id, type, message, timeout });

        const timer = setTimeout(() => {
            let index = _notifications.findIndex((item) => item.id == id);
            _notifications.splice(index, 1);
        }, timeout);
    }

    let timers = [];

    const notifications = $derived(_notifications);

    return {
        notifications,
        send,
        default: (msg, timeout) => send(msg, "default", timeout),
        danger: (msg, timeout) => send(msg, "danger", timeout),
        warning: (msg, timeout) => send(msg, "warning", timeout),
        info: (msg, timeout) => send(msg, "info", timeout),
        success: (msg, timeout) => send(msg, "success", timeout),
    };
}

export const notifications = createNotificationStore();
