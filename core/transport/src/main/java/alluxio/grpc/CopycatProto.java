// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/copycat.proto

package alluxio.grpc;

public final class CopycatProto {
  private CopycatProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_alluxio_grpc_copycat_CopycatRequestHeader_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_alluxio_grpc_copycat_CopycatRequestHeader_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_alluxio_grpc_copycat_CopycatResponseHeader_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_alluxio_grpc_copycat_CopycatResponseHeader_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_alluxio_grpc_copycat_CopycatMessage_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_alluxio_grpc_copycat_CopycatMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022grpc/copycat.proto\022\024alluxio.grpc.copyc" +
      "at\")\n\024CopycatRequestHeader\022\021\n\trequestId\030" +
      "\001 \001(\003\":\n\025CopycatResponseHeader\022\021\n\treques" +
      "tId\030\001 \001(\003\022\016\n\006failed\030\002 \001(\010\"\251\001\n\016CopycatMes" +
      "sage\022A\n\rrequestHeader\030\001 \001(\0132*.alluxio.gr" +
      "pc.copycat.CopycatRequestHeader\022C\n\016respo" +
      "nseHeader\030\002 \001(\0132+.alluxio.grpc.copycat.C" +
      "opycatResponseHeader\022\017\n\007message\030\003 \001(\0142q\n" +
      "\024CopycatMessageServer\022Y\n\007connect\022$.allux" +
      "io.grpc.copycat.CopycatMessage\032$.alluxio" +
      ".grpc.copycat.CopycatMessage(\0010\001B\036\n\014allu" +
      "xio.grpcB\014CopycatProtoP\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_alluxio_grpc_copycat_CopycatRequestHeader_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_alluxio_grpc_copycat_CopycatRequestHeader_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_alluxio_grpc_copycat_CopycatRequestHeader_descriptor,
        new java.lang.String[] { "RequestId", });
    internal_static_alluxio_grpc_copycat_CopycatResponseHeader_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_alluxio_grpc_copycat_CopycatResponseHeader_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_alluxio_grpc_copycat_CopycatResponseHeader_descriptor,
        new java.lang.String[] { "RequestId", "Failed", });
    internal_static_alluxio_grpc_copycat_CopycatMessage_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_alluxio_grpc_copycat_CopycatMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_alluxio_grpc_copycat_CopycatMessage_descriptor,
        new java.lang.String[] { "RequestHeader", "ResponseHeader", "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
