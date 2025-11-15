// src/components/Layout.jsx
import React from 'react';

const Layout = ({ children, imageUrl, imagePosition = 'right' }) => {
  const isMobile = window.innerWidth < 768;

  return (
    <div className="layout-container">
      {!isMobile && imagePosition === 'left' && (
        <div className="layout-image" style={{ backgroundImage: `url(${imageUrl})` }} />
      )}

      <div className="layout-content">
        {children}
      </div>

      {!isMobile && imagePosition === 'right' && (
        <div className="layout-image" style={{ backgroundImage: `url(${imageUrl})` }} />
      )}

      {isMobile && (
        <div className="layout-image" style={{ backgroundImage: `url(${imageUrl})` }} />
      )}
    </div>
  );
};

export default Layout;