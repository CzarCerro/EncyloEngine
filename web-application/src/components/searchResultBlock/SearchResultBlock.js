import styles from './styles/searchresultblock.module.css';

function SearchResultBlock({ title, url, content }) {
  const MAX_WORDS = 470; // Maximum number of words to display

  const truncateContent = (text, maxCharacters) => {
    if (text.length > maxCharacters) {
      return text.slice(0, maxCharacters) + '...';
    }
    return text;
  };

  return (
    <div className={styles.SearchResultBlock} onClick={() => { window.location.href = url; }}>
      <a href={url}>{url}</a>
      <h2>{title}</h2>
      <span>{truncateContent(content, MAX_WORDS)}</span>
    </div>
  );
}

export default SearchResultBlock;
