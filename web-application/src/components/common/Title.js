function Title({ fontSize }) {
    const titleFontSize = fontSize !== null ? `${fontSize}rem` : '5rem';
  
    return (
      <h1 style={{ fontSize: titleFontSize, cursor: 'pointer' }} onClick={() => { 
        localStorage.clear();
        window.location.href = '/'; 
      }}>EncycloEngine</h1>
    );
  }
  
  export default Title;
  