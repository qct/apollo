INSERT INTO App (AppId, Name, OwnerName, OwnerEmail) VALUES ('apollo-config-service','apollo-config-service','刘一鸣','liuym@ctrip.com');
INSERT INTO App (AppId, Name, OwnerName, OwnerEmail) VALUES ('apollo-admin-service','apollo-admin-service','宋顺','song_s@ctrip.com');
INSERT INTO App (AppId, Name, OwnerName, OwnerEmail) VALUES ('apollo-portal','apollo-portal','张乐','zhanglea@ctrip.com');
INSERT INTO App (AppId, Name, OwnerName, OwnerEmail) VALUES ('fxhermesproducer','fx-hermes-producer','梁锦华','jhliang@ctrip.com');

INSERT INTO Cluster (AppId, Name) VALUES ('apollo-config-service', 'default');
INSERT INTO Cluster (AppId, Name) VALUES ('apollo-config-service', 'cluster1');
INSERT INTO Cluster (AppId, Name) VALUES ('apollo-admin-service', 'default');
INSERT INTO Cluster (AppId, Name) VALUES ('apollo-admin-service', 'cluster2');
INSERT INTO Cluster (AppId, Name) VALUES ('apollo-portal', 'default');
INSERT INTO Cluster (AppId, Name) VALUES ('apollo-portal', 'cluster3');
INSERT INTO Cluster (AppId, Name) VALUES ('fxhermesproducer', 'default');

INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-config-service', 'application');
INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-config-service', 'fx.apollo.config');
INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-admin-service', 'application');
INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-admin-service', 'fx.apollo.admin');
INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-portal', 'application');
INSERT INTO AppNamespace (AppId, Name) VALUES ('apollo-portal', 'fx.apollo.portal');
INSERT INTO AppNamespace (AppID, Name) VALUES ('fxhermesproducer', 'fx.hermes.producer');

INSERT INTO Namespace (Id, AppId, ClusterName, NamespaceName) VALUES (1, 'apollo-config-service', 'default', 'application');
INSERT INTO Namespace (Id, AppId, ClusterName, NamespaceName) VALUES (5, 'apollo-config-service', 'cluster1', 'application');
INSERT INTO Namespace (Id, AppId, ClusterName, NamespaceName) VALUES (2, 'fxhermesproducer', 'default', 'fx.hermes.producer');
INSERT INTO Namespace (Id, AppId, ClusterName, NamespaceName) VALUES (3, 'apollo-admin-service', 'default', 'application');
INSERT INTO Namespace (Id, AppId, ClusterName, NamespaceName) VALUES (4, 'apollo-portal', 'default', 'application');

INSERT INTO Item (NamespaceId, `Key`, Value, Comment) VALUES (1, 'k1', 'v1', 'comment1');
INSERT INTO Item (NamespaceId, `Key`, Value, Comment) VALUES (1, 'k2', 'v2', 'comment2');
INSERT INTO Item (NamespaceId, `Key`, Value, Comment) VALUES (2, 'k3', 'v3', 'comment3');
INSERT INTO Item (NamespaceId, `Key`, Value, Comment, LineNum) VALUES (5, 'k1', 'v4', 'comment4',1);

INSERT INTO RELEASE (ReleaseKey, Name, Comment, AppId, ClusterName, NamespaceName, Configurations) VALUES ('TEST-RELEASE-KEY', 'REV1','First Release','apollo-config-service', 'default', 'application', '{"k1":"v1"}');

