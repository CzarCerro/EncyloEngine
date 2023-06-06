function Title({ fontSize }) {
    const titleFontSize = fontSize !== null ? `${fontSize}rem` : '5rem';
  
    return (
      <h1 style={{ fontSize: titleFontSize }}>EncycloEngine</h1>
    );
  }
  
  export default Title;
  