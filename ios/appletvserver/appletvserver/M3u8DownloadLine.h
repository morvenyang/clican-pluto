//
//  M3u8DownloadLine.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

@interface M3u8DownloadLine : NSObject{
    NSString* _originalUrl;
    NSString* _localUrl;
    NSString* _localPath;
    BOOL _finished;
}

@property (nonatomic, copy) NSString* originalUrl;
@property (nonatomic, copy) NSString* localUrl;
@property (nonatomic, copy) NSString* localPath;
@property (nonatomic, assign) BOOL finished;

@end
