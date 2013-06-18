//
//  DownloadProgressViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-4-23.
//
//
#ifndef DownloadProgressViewController_h
#define DownloadProgressViewController_h
#define RECT_SAPCE 5
#define RECT_SIZE 10
#endif

#import "DownloadProgressViewController.h"
#import "AppDelegate.h"
#import "ffmpeg.h"
#import "AtvUtil.h"
#import "CricleUIView.h"

@implementation DownloadProgressViewController

@synthesize refreshTimer = _refreshTimer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"代理下载";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"代理下载" image:nil tag:1] autorelease];
    }
    return self;
}
-(void)dealloc{
    TT_RELEASE_SAFELY(_refreshTimer);
    [super dealloc];
}
-(void)refreshView{
    M3u8Process* m3u8Proess = [AppDele m3u8Process];
    Mp4Process* mp4Proess = [AppDele mp4Process];
    CGRect frame = [UIScreen mainScreen].applicationFrame;
    int countPerLine = (int)(frame.size.width-RECT_SAPCE)/(RECT_SAPCE+RECT_SIZE);
    CGFloat y = frame.origin.y+RECT_SAPCE;
    NSArray* subviews=self.view.subviews;
    for(UIView* subview in subviews){
        [subview removeFromSuperview];
    }
    long tempSize = 0;
    if(m3u8Proess.running){
        tempSize = [m3u8Proess.m3u8Download getDurationTempSize]/(1024*3);
        [self updateTempSize:tempSize];
        for(int i=0;i<[m3u8Proess.m3u8Download.m3u8DownloadLines count];i++){
            M3u8DownloadLine* line =[m3u8Proess.m3u8Download.m3u8DownloadLines objectAtIndex:i];
            if(i%countPerLine==0&&i!=0){
                y = y+RECT_SAPCE+RECT_SIZE;
            }
            CGFloat x = i%countPerLine*(RECT_SAPCE+RECT_SIZE)+RECT_SAPCE;
            CGRect rect = CGRectMake(x,y,RECT_SIZE,RECT_SIZE);
            if(line.finished){
                CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor greenColor]] autorelease];
                [self.view addSubview:cricle];
            }else{
                CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor grayColor]] autorelease];
                [self.view addSubview:cricle];
            }
        }
    }else if(mp4Proess.running){
        tempSize = [mp4Proess.mp4Download getDurationTempSize]/(1024*3);
        [self updateTempSize:tempSize];
        for(int i=0;i<[mp4Proess.mp4Download.mp4DownloadPartials count];i++){
            Mp4DownloadPartial* partial =[mp4Proess.mp4Download.mp4DownloadPartials objectAtIndex:i];
            if(i%countPerLine==0&&i!=0){
                y = y+RECT_SAPCE+RECT_SIZE;
            }
            CGFloat x = i%countPerLine*(RECT_SAPCE+RECT_SIZE)+RECT_SAPCE;
            CGRect rect = CGRectMake(x,y,RECT_SIZE,RECT_SIZE);
            if(partial.finished){
                CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor greenColor]] autorelease];
                [self.view addSubview:cricle];
            }else{
                CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor grayColor]] autorelease];
                [self.view addSubview:cricle];
            }
        }
    }else if(transfer_code_interrupt==0){
        tempSize= 0;
        NSString* mkvOutPath = AppDele.localMkvM3u8PathPrefix;
        NSString* mkvM3u8File = [AppDele.localMp3PathPrefix stringByAppendingString:@"mkv.m3u8"];
        if([[NSFileManager defaultManager] fileExistsAtPath:mkvM3u8File]){
            NSString* content = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:mkvM3u8File] encoding:NSUTF8StringEncoding error:nil];
            
            NSArray* array = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:mkvOutPath error:nil];
            int count = [array count];
            if(![AtvUtil content:content contains:@"#EXT-X-ENDLIST"]){
                count = count*2;
                if(count<100){
                    count = 100;
                }
            }
            for(int i=0;i<count;i++){
                if(i%countPerLine==0&&i!=0){
                    y = y+RECT_SAPCE+RECT_SIZE;
                }
                CGFloat x = i%countPerLine*(RECT_SAPCE+RECT_SIZE)+RECT_SAPCE;
                CGRect rect = CGRectMake(x,y,RECT_SIZE,RECT_SIZE);
                if(i<[array count]){
                    CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor greenColor]] autorelease];
                    [self.view addSubview:cricle];
                }else{
                    CricleUIView* cricle = [[[CricleUIView alloc] initWithFrame:rect color:[UIColor grayColor]] autorelease];
                    [self.view addSubview:cricle];
                    
                }
            }
        }else{
            UILabel* label = [[[UILabel alloc] initWithFrame:CGRectMake(10, 10, frame.size.width-20, 32)] autorelease];
            label.text = @"没有正在执行的任务";
            [self.view addSubview:label];
        }
    }else{
        UILabel* label = [[[UILabel alloc] initWithFrame:CGRectMake(10, 10, frame.size.width-20, 32)] autorelease];
        label.text = @"没有正在执行的任务";
        [self.view addSubview:label];
    }
}
-(void)loadView {
    [super loadView];
    [self.view setBackgroundColor:[UIColor whiteColor]];
    [self refreshView];
}
-(void)viewWillAppear:(BOOL)animated{
    if(self.refreshTimer==nil||![self.refreshTimer isValid]){
        NSLog(@"start refresh timer for download progress");
        self.refreshTimer = [NSTimer timerWithTimeInterval:3.0f target:self selector:@selector(refreshView) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.refreshTimer forMode:NSRunLoopCommonModes];
    }
}
-(void)viewDidDisappear:(BOOL)animated{
    NSLog(@"end refresh timer for download progress");
    [self.refreshTimer invalidate];
}

-(void)updateTempSize:(long)tempSize{
    TTButton* button = [TTButton buttonWithStyle:@"toolbarRoundButton:" title:[NSString stringWithFormat:@"%ldKB/S",tempSize]];
    
    [button setFont:[UIFont systemFontOfSize:14]];
    
    [button sizeToFit];
    
    UIBarButtonItem* item = [[UIBarButtonItem alloc] initWithCustomView:button];
    
    [self.navigationItem setLeftBarButtonItem:item animated:YES];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
	TTButton* cancelButton = [TTButton buttonWithStyle:@"toolbarRoundButton:" title:@"取消当前任务"];
    
    [cancelButton setFont:[UIFont systemFontOfSize:14]];
    
    [cancelButton addTarget:self action:@selector(cancelAction) forControlEvents:UIControlEventTouchUpInside];
    [cancelButton sizeToFit];
    
    UIBarButtonItem* refreshItem = [[UIBarButtonItem alloc] initWithCustomView:cancelButton];
    
    [self.navigationItem setRightBarButtonItem:refreshItem animated:YES];
}

- (void)cancelAction{
    AppDele.m3u8Process.running =NO;
    AppDele.mp4Process.running =NO;
    transfer_code_interrupt = 1;
    AppDele.m3u8Process.m3u8Url=nil;
    AppDele.mp4Process.mp4Url=nil;
    AppDele.mkvProcess.mkvUrl = nil;
    [[AppDele queue] cancelAllOperations];
    [[AppDele queue] waitUntilAllOperationsAreFinished];
    [[AppDele queue] go];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
