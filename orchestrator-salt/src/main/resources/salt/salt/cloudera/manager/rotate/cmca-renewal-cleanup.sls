/opt/salt/scripts/rotate/cmca_renewal_cleanup.sh:
  file.managed:
    - makedirs: True
    - mode: 700
    - source: salt://cloudera/manager/scripts/cmca_renewal_cleanup.sh
    - template: jinja

renew-cmca:
  cmd.run:
    - name: /opt/salt/scripts/rotate/cmca_renewal_cleanup.sh 2>&1 | tee -a /var/log/cm_cmca_renewal_cleanup.log && exit ${PIPESTATUS[0]}
    - require:
      - file: /opt/salt/scripts/rotate/cmca_renewal_cleanup.sh