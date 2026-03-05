const Placeholder = ({ title, description }) => (
  <div>
    <div className="page-header">
      <div>
        <h1>{title}</h1>
        <p>{description}</p>
      </div>
    </div>
    <div
      className="card"
      style={{ textAlign: "center", padding: "var(--space-2xl)" }}
    >
      <div style={{ fontSize: "3rem", marginBottom: "var(--space-md)" }}>
        🚧
      </div>
      <h2
        style={{
          color: "var(--text-secondary)",
          marginBottom: "var(--space-sm)",
        }}
      >
        Coming in Phase 8
      </h2>
      <p style={{ color: "var(--text-muted)" }}>
        This module will be built in the next phase.
      </p>
    </div>
  </div>
);

export default Placeholder;
