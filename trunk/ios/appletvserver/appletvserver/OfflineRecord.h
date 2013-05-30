//
//  OfflineRecord.h
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import <Foundation/Foundation.h>

@interface OfflineRecord : NSObject{
    NSString* _url;
    long _fileSize;
    NSString* _fileName;
    NSString* _filePath;
    NSString* _fileType;
}
@property (nonatomic, copy) NSString* url;
@property (nonatomic, copy) NSString* fileName;
@property (nonatomic, copy) NSString* filePath;
@property (nonatomic, copy) NSString* fileType;
@property (nonatomic, assign) long fileSize;
@end
