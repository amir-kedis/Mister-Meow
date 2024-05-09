export interface CloseIconProps {
  className?: string;
  rest?: any;
}

export default function CloseIcon({ className, ...rest }: CloseIconProps) {
  return (
    <svg
      className={`stroke-icon ${className}`}
      width="14"
      height="14"
      viewBox="0 0 14 14"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      {...rest}
    >
      <path
        d="M1 13L13 1M1 1L13 13"
        stroke="current"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}
