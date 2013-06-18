//
//  OfflineRecord.h
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"

@interface OfflineRecord : NSObject<ASIProgressDelegate>{
    NSString* _url;
    long _downloadFileSize;
    long _fileSize;
    NSString* _fileName;
    NSString* _displayName;
    NSString* _filePath;
    NSString* _fileType;
    ASIHTTPRequest* _request;
    BOOL _downloading;
    long _tempSize;
}
@property (nonatomic, copy) NSString* url;
@property (nonatomic, copy) NSString* fileName;
@property (nonatomic, copy) NSString* displayName;
@property (nonatomic, copy) NSString* filePath;
@property (nonatomic, copy) NSString* fileType;
@property (nonatomic, retain) ASIHTTPRequest* request;
@property (nonatomic, assign) long fileSize;
@property (nonatomic, assign) long downloadFileSize;
@property (nonatomic, assign) BOOL downloading;
@property (atomic, assign) long tempSize;
@end
