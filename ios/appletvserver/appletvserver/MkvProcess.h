//
//  MkvProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-4-5.
//
//

#import <Foundation/Foundation.h>

@interface MkvProcess : NSObject{
    NSString* _mkvUrl;
    BOOL _running;
    NSString* _mkvM3u8Content;
}

@property (nonatomic, copy) NSString* mkvUrl;
@property (nonatomic, assign) BOOL running;
@property (nonatomic, copy) NSString* mkvM3u8Content;
-(void) convertToM3u8MkvUrl:(NSString*) url;

@end
