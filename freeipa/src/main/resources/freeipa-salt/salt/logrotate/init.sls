logrotate-krb5kdc:
  file.managed:
    - name: /etc/logrotate.d/krb5kdc
    - source: salt://logrotate/conf/krb5kdc
    - user: root
    - group: root
    - mode: 644