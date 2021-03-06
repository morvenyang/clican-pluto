//
//  ConfigDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-4-1.
//
//

#import <Three20UI/Three20UI.h>
@protocol ConfigCallback
-(void) updateConfig;
@end

@class ConfigCallback;

@interface ConfigDataSource : TTSectionedDataSource{
    ConfigCallback* _callback;
}
@property (nonatomic, retain) ConfigCallback    *callback;
+ (ConfigDataSource*)dataSourceWithArrays:(NSArray*)array;
@end
