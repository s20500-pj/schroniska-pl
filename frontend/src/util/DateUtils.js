export function formatDate(rawDate) {
    if (!rawDate) {
        return '';
    }

    const date = new Date(rawDate);

    return date.toLocaleDateString('pl-PL', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });
}

export function formatDateWithTime(rawDate) {
    if (!rawDate) {
        return '';
    }
    const [year, month, day, hour, minute] = rawDate;
    const date = new Date(year, month - 1, day, hour, minute);
    return date.toLocaleString('pl-PL', {dateStyle: 'short', timeStyle: 'short'});
}